from django.urls import path

from . import views

urlpatterns = [
    path('users/me/', views.UserMe.as_view()),
    path('users/all/<int:project_id>/<int:offset>/', views.Users.as_view()),
    path('users/get/<int:pk>/', views.GetUser.as_view()),
    path('projects/<int:offset>/', views.Projects.as_view()),
    path('projects/get/<int:pk>/', views.GetProject.as_view()),
    path('projects/all/<int:offset>/', views.AllProjects.as_view()),
    path('projects/get/<int:pk>/invite/<int:user_id>/', views.InviteUser.as_view()),
    path('projects/get/<int:pk>/join/', views.JoinProject.as_view()),
    path('activities/reply/<int:pk>/<int:reply>/', views.ReplyRequest.as_view()),
    path('activities/get/<int:offset>/', views.GetActivities.as_view()),
    path('chat/get/all/', views.AllChat.as_view()),
    path('chat/get/<int:user_id>/<int:offset>/', views.Chat.as_view()),
]
                                                        
