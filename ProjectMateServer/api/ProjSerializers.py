from rest_framework import serializers
from api.models import *


class ProjectSkillSerializer(serializers.ModelSerializer):

    class Meta:
        model = ProjectSkill
        fields = ('skill',)

class SkillSerializer(serializers.ModelSerializer):

    class Meta:
        model = Skill
        fields = ('skill_id', 'skill_name', 'skill_rating', 'skill_short_desc', 'skill_courses_taken')


class ProjectSerializer(serializers.ModelSerializer):

    skills = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='skill'
     )

    class Meta:
        model = Project
        fields = ('id', 'project_name', 'project_short_desc', 'project_complete_desc', 'skills')


class UserSerializer(serializers.ModelSerializer):
    skills = SkillSerializer(many=True, read_only=True)
    projects = ProjectSerializer(many=True, read_only=True)

    class Meta:
        model = User
        fields = ('id', 'name', 'organization', 'city', 'location', 'username', 'ranking', 'skills', 'projects')


class NewProjectSkillSerializer(serializers.ModelSerializer):

    class Meta:
        model = ProjectSkill
        fields = ('skill',)


class NewProjectSerializer(serializers.ModelSerializer):

    skills = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='skill'
     )

    class Meta:
        model = Project
        fields = ('id', 'project_name', 'project_short_desc', 'project_complete_desc', 'skills')


class BasicUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username')


class BasicUserSkillSerializer(serializers.ModelSerializer):
    skills = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='skill_id'
     )
    class Meta:
        model = User
        fields = ('id', 'username', 'skills')

class BasicProjectSerializer(serializers.ModelSerializer):
    class Meta:
        model = NewProject
        fields = ('id', 'project_name')


class MessageSerializer(serializers.ModelSerializer):
    """For Serializing Message"""
    sender = BasicUserSerializer(read_only=True)
    receiver = BasicUserSerializer(read_only=True)
    class Meta:
        model = Message
        fields = ('sender', 'receiver', 'message')



class ActivitySerializer(serializers.ModelSerializer):
    sender = BasicUserSerializer(read_only=True)
    receiver = BasicUserSerializer(read_only=True)

    project = BasicProjectSerializer(read_only=True)

    class Meta:
        model = Activities
        fields = ('id', 'sender','receiver', 'project', 'activity_type')
