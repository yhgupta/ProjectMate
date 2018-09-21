import requests, json

from api.models import *
from api.ProjSerializers import *

from django.core import serializers

ERROR = {"Error" : "Authentication Failed"}

DATA_EROOR = {"Error" : "Invalid Request"}

class UserHelper:

    @staticmethod
    def createNewUser(data, auth_id):

        #Remove skills and projects fields
        user_data = data
        user_data.pop('skills')
        user_data.pop('projects')

        #Create a new user with CodeChef data
        serializer = UserSerializer(data=user_data)
        if serializer.is_valid():
            user = serializer.save()
            user.auth_id = auth_id
            user.save()
            print(serializer.data)
            return [True, user]

        return [False, ""]

    @staticmethod
    def getUserAsDict(user):
        return UserSerializer(instance=user).data

    
    @staticmethod
    def updateUserSkillsAndProjects(user, data):
        try:
            skills = data['skills']
            projects = data['projects']

            #Very Dangerous way
            user.skills.all().delete()
            user.projects.all().delete()


            for skill in skills:
                Skill.objects.create(user=user, **skill)

            print("Skills done")

            for project in projects:
                project.pop('id')
                project['display'] = False
                proj_skills = project.pop('skills')
                print(project)
                project2 = Project.objects.create(user=user, **project)

                for skill in proj_skills:
                    print(skill)
                    st = {"skill":skill}
                    ProjectSkill.objects.create(project=project2, **st)



            print("proj done")



            return {'Success':'Updated Sucessfully'}

        except Exception as e:
            print(e)
            return DATA_EROOR
                



class VerifyHelper:

    @staticmethod
    def isUserVerified(request):
        auth_id = ""
        try:
            auth_id = request.META["HTTP_AUTHORIZATION"]
        except:
            return [False, ERROR]

        try:
            user = User.objects.get(auth_id=auth_id)
            return [True, user]

        except User.DoesNotExist:
            return [False, ERROR]
        

    @staticmethod
    def verifyUser(request):
        auth_id = ""
        try:
            auth_id = request.META["HTTP_AUTHORIZATION"]
        except:
            return [False, ERROR]

        print("Got auth_id %s" %auth_id)

        try:
            user = User.objects.get(auth_id=auth_id)
            return [True, UserSerializer(instance=user).data, user]

        except User.DoesNotExist:
            print("User with auth_id not found, getting user from codeChef")
            return VerifyHelper.getUser(auth_id)

    @staticmethod
    def getUser(auth_id):
        
            codeChefResponse = VerifyHelper.getUserFromCodeChef(auth_id)

            if(codeChefResponse['status']=="OK"):

                print("CodeChef Response OK")

                formattedResponse = VerifyHelper.formatCodeChefRespone(codeChefResponse)
                print(formattedResponse)

                try:
                    user = User.objects.get(username=formattedResponse['username'])
                    print("Got User %s" %user.username)

                    user.auth_id = auth_id
                    user.save()

                    return [True, UserHelper.getUserAsDict(user), user]
                
                except User.DoesNotExist:
                    print("User with username also does not exists")

                    created_user = UserHelper.createNewUser(formattedResponse, auth_id)
                    if created_user[0]:
                        print(formattedResponse)
                        return [True, UserSerializer(created_user), created_user[1]]

            else:
                print("CodeChef Response Error")
                return [False, ERROR]



    @staticmethod
    def getUserFromCodeChef(auth_id):
        headers = {"Accept" : "application/json",
        "Authorization" : "Bearer " + auth_id}
        response = requests.get("https://api.codechef.com/users/me", headers=headers)
        return json.loads(response.text)


    @staticmethod
    def formatCodeChefRespone(codeChefResponse):
        content = codeChefResponse['result']['data']['content']
        username = content['username']
        name = content['fullname']
        city = content['city']['name']
        country = content['country']['name']
        state = content['state']['name']
        organization = content['organization']
        ranking = content['rankings']['allContestRanking']['global']

        skills = []
        projects = []
	
        filteredResponse = {}
        filteredResponse['username'] = username
        filteredResponse['name'] = name
        filteredResponse['city'] = city
        filteredResponse['location'] = state + ", " + country
        filteredResponse['organization'] = organization
        filteredResponse['ranking'] = ranking
        filteredResponse['skills'] = skills
        filteredResponse['projects'] = projects

        return filteredResponse