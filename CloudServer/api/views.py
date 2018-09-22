from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from api.models import *
from django.db.models import Q, Case, F, When

from datetime import datetime

from api.ProjSerializers import *

from api.helper import VerifyHelper, UserHelper
import requests, json

ACTIVITY_TYPE_REQUEST_INVITE = 500
ACTIVITY_TYPE_REQUEST_JOIN = 501

ACTIVITY_TYPE_REQUEST_INVITE_ACCEPTED = 503
ACTIVITY_TYPE_REQUEST_JOIN_ACCEPTED = 504

ACTIVITY_TYPE_REQUEST_INVITE_REJECTED = 505
ACTIVITY_TYPE_REQUEST_JOIN_REJECTED = 506




class UserMe(APIView):

    def get(self, request, format=None):
        return Response(VerifyHelper.verifyUser(request)[1])

    def put(self, request, format=None):
        verify = VerifyHelper.verifyUser(request)

        if verify[0]:
            user = verify[2]
            return Response(UserHelper.updateUserSkillsAndProjects(user, request.data))

        else:
            return Response(verify[1])


class GetProject(APIView):

    def get(self, request, pk, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        project = NewProject.objects.get(pk=pk)

        return Response(NewProjectSerializer(project).data)

class Projects(APIView):
    
    def get(self, request, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        skills = []

        for skill in user.skills.all():
            skills.append(skill.skill_id)
        
        projects = NewProject.objects.filter(id__in=NewProjectSkill.objects.values('project').filter(skill__in=skills)).filter(visible=True)[offset:offset+15]

        return Response(NewProjectSerializer(projects, many=True).data)


class AllProjects(APIView):
    
    def get(self, request, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        
        projects = NewProject.objects.filter(visible=True).exclude(user=user)[offset:offset+15]

        return Response(NewProjectSerializer(projects, many=True).data)

    
    def put(self, request, offset, format=None):

        #offset will be useful later

        verified, user = VerifyHelper.isUserVerified(request)
        if not verified:
            return Response(user)

        NewProject.objects.all().delete()

        for project in request.data:
            project.pop('id')
            skills = project.pop('skills')
            proj = NewProject.objects.create(user=user, **project)

            for skill in skills:
                skill_fmtd = {'skill':skill}
                NewProjectSkill.objects.create(project=proj, **skill_fmtd)
        
        return Response({"Success":"Successfully updated"})


class Users(APIView):

    def get(self, request, project_id, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        
        project = NewProject.objects.get(pk=project_id)

        skills = []

        for skill in project.skills.all():
            skills.append(skill.skill)

        print(skills)
        
        users = User.objects.filter(id__in=Skill.objects.values('user').filter(skill_id__in=skills)).exclude(pk=user.pk)[offset:offset+15]

        return Response(BasicUserSkillSerializer(users, many=True).data)


class GetUser(APIView):

    def get(self, request, pk, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        user = User.objects.get(pk=pk)

        return Response(UserSerializer(user).data)

class InviteUser(APIView):

    def get(self, request, pk, user_id, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        project = NewProject.objects.get(pk=pk)
        toUser = User.objects.get(pk=user_id)

        data = {'activity_type':ACTIVITY_TYPE_REQUEST_INVITE}

        prevActivity = Activities.objects.filter(project=project, sender=user)
        
        if(len(prevActivity) > 0): 
            return Response({"Error":"Activity Already Done"})

        activity = Activities.objects.create(sender=user, receiver=toUser, project=project, **data)
    
        return Response(ActivitySerializer(activity).data)


class JoinProject(APIView):

    def get(self, request, pk, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        project = NewProject.objects.get(pk=pk)
        toUser = User.objects.get(new_projects=project)

        data = {'activity_type':ACTIVITY_TYPE_REQUEST_JOIN}

        prevActivity = Activities.objects.filter(project=project, sender=user)
        
        if(len(prevActivity) > 0): 
            return Response({"Error":"Activity Already Done"})

        activity = Activities.objects.create(sender=user, receiver=toUser, project=project, **data)
        
        return Response(ActivitySerializer(activity).data)


class GetActivities(APIView):   

    def get(self, request, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        activities = Activities.objects.filter(Q(sender=user) | Q(receiver=user))

        return Response(ActivitySerializer(activities, many=True).data)


class ReplyRequest(APIView):

    def get(self, request, pk, reply, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        activity = Activities.objects.get(pk=pk)

        if activity.receiver == user:
            if activity.activity_type == ACTIVITY_TYPE_REQUEST_INVITE:
                if(reply==100):
                    activity.activity_type = ACTIVITY_TYPE_REQUEST_INVITE_ACCEPTED
                else:
                    activity.activity_type = ACTIVITY_TYPE_REQUEST_INVITE_REJECTED

                activity.timestamp=datetime.datetime.now()

                activity.save()

                return Response(ActivitySerializer(activity).data)

            if activity.activity_type == ACTIVITY_TYPE_REQUEST_JOIN:

                if(reply==100):
                    activity.activity_type = ACTIVITY_TYPE_REQUEST_JOIN_ACCEPTED
                else:
                    activity.activity_type = ACTIVITY_TYPE_REQUEST_JOIN_REJECTED

                activity.timestamp=datetime.datetime.now()

                activity.save()

                return Response(ActivitySerializer(activity).data)

        
        return Response("Invalid")


class Chat(APIView):

    def get(self, request, user_id, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)
        
        toUser = User.objects.get(pk=user_id)

        messages = Message.objects.filter(Q(sender=user, receiver=toUser) | Q(sender=toUser, receiver=user))[offset:offset+15]

        return Response(MessageSerializer(messages, many=True).data)

    def put(self, request, user_id, offset, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)

        receiver = User.objects.get(pk=user_id)
        print(request.data)
        message = request.data['message']
        print(message)

        message = {'message':message}

        formed_message = Message.objects.create(sender=user, receiver=receiver, **message)

        return Response(MessageSerializer(formed_message).data)


class AllChat(APIView):

    def get(self, request, format=None):

        verified, user = VerifyHelper.isUserVerified(request)
        
        if not verified:
            return Response(user)
        

        diff_users = Message.objects.filter(Q(sender=user) | Q(receiver=user)).annotate(other=Case(When(sender=user, then=F('receiver')), default=F('sender'))).values('other').distinct().order_by()

        message_ids = set()
        for other in diff_users:
            message = Message.objects.filter((Q(sender=user) & Q(receiver=other['other']))|(Q(receiver=user) & Q(sender=other['other'])))[:1]
            message_ids.add(message[0].pk)

        messages = Message.objects.filter(id__in=message_ids)
        return Response(MessageSerializer(messages, many=True).data)