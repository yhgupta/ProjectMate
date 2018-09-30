import datetime

from django.db import models
from django.utils import timezone

#This is the User table with given columns
class User(models.Model):

    #Storing the codeChef access key of the user to identify him
    auth_id = models.CharField(max_length=200)

    #All these details are obtained from CodeChef API
    name = models.CharField(max_length=200)
    organization = models.CharField(max_length=200)
    city = models.CharField(max_length=200)
    location = models.CharField(max_length=200)

    username = models.CharField(max_length=100)
    ranking = models.IntegerField(default=0)
    

    def __str__(self):
        return self.username

#This is the Project table to store the projects user has done (as seen in his profile)
class Project(models.Model):
    #A project belongs to a particular user and should be deleted on user delete
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="projects")

    project_name = models.CharField(max_length=200)
    project_short_desc = models.CharField(max_length=250)
    project_complete_desc = models.TextField()

    def __str__(self):
        return self.project_name

#This is the Skill table to store the skills user have (as seen in his profile)
class Skill(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="skills")

    skill_id = models.IntegerField(default=0)
    skill_name = models.CharField(max_length=200)
    skill_rating = models.FloatField(default=5.0)
    skill_short_desc = models.CharField(max_length=250)
    skill_courses_taken = models.TextField()

    def __str__(self):
        return self.skill_name

#A table to interlink project and skill as a project can have multiple skills
#Skills are identified by skill_Id which is unique for every skill
class ProjectSkill(models.Model):
    project = models.ForeignKey(Project, on_delete=models.CASCADE, related_name="skills")
    skill = models.IntegerField()
    
    def __str__(self):
        return str(self.skill)

    def __unicode__(self):
        return str(self.skill)

#This is the table which store new projects (which are visible to other people), You can send a join/invite request for these projects
class NewProject(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="new_projects")

    project_name = models.CharField(max_length=200)
    project_short_desc = models.CharField(max_length=250)
    project_complete_desc = models.TextField()
    
    visible = models.BooleanField(default=True)

    def __str__(self):
        return self.project_name

#A table to interlink new project and skill as a project can have multiple skills required
class NewProjectSkill(models.Model):
    project = models.ForeignKey(NewProject, on_delete=models.CASCADE, related_name="skills")
    skill = models.IntegerField()
    
    def __str__(self):
        return str(self.skill)

    def __unicode__(self):
        return str(self.skill)


#A table to store messages between two users
class Message(models.Model):
    sender = models.ForeignKey(User, on_delete=models.CASCADE, related_name='sender')        
    receiver = models.ForeignKey(User, on_delete=models.CASCADE, related_name='receiver')        
    message = models.CharField(max_length=1200)
    timestamp = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.message
    class Meta:
        ordering = ('-timestamp',)

#Stores activities for a user E.g. if he receives a invite request
class Activities(models.Model):
    sender = models.ForeignKey(User, on_delete=models.CASCADE, related_name='activity_sender')        
    receiver = models.ForeignKey(User, on_delete=models.CASCADE, related_name='activity_receiver')
    project = models.ForeignKey(NewProject, on_delete=models.CASCADE, related_name="activities")

    activity_type = models.IntegerField()
    timestamp = models.DateTimeField(auto_now_add=True, editable=True)

    class Meta:
        ordering = ('-timestamp',)

