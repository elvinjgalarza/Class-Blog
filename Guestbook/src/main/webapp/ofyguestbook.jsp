<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import = "com.googlecode.objectify.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.List" %>
<%@ page import = "java.util.Collections" %>
<%@ page import = "guestbook.Greeting" %>
 

<html>

  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
  
  <style>body,h1,h2,h3,h4,h5 {font-family: "Raleway", sans-serif}</style>  
  <body class="w3-light-grey">
  
  
  <!-- 
  <head>
   <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
  <body> -->
  
  <!-- HEADER -->
  <header class ="w3-container w3-center w3-padding-10">
  <h1><b>The Boring Blog</b></h1> 
  <p>by Elvin and Sopheara</p>
  <img  src = "jeff.gif" class ="w3-circle w3-animate-fading" alt= "jeff" style = "width:30%">
  </header> 
  
  
<%
    String guestbookName = request.getParameter("guestbookName");
    if (guestbookName == null) {
        guestbookName = "default";
    }
    pageContext.setAttribute("guestbookName", guestbookName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
      pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } 
    else {
%>

<div class ="w3-container w3-center w3-padding-10">
<p>This blog... uh... finds a way.
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to post... clever gorl!</p>
</div>

<%
    }
%>

 <%
	//only show if user signed in
	//it's above so they don't have to scroll down everytime they post
 	if(user != null){
 %>
 
	<form action="/ofyguestbook" method="post">
      <p>Title</p>
      <div><textarea name = "title" rows = "1" cols = "30"></textarea></div>
      <p>Content</p>
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Post"> 
      	<input type="submit" name="clear" value = "Clear">
      	</div>
      <div><input type="submit" name="sub" value = "Subscribe"></div>
      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
    </form>
 <% 
 	}
 %>
<%
    Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    // Register the Greeting Entity, 
	ObjectifyService.register(Greeting.class);
    // grab the greetings,
	List<Greeting> g = ObjectifyService.ofy().load().type(Greeting.class).list();   
	// then sort them 
    Collections.sort(g);
    List<Greeting> greetings;
	if(g.size() > 5){
		greetings = g.subList(g.size()-5,g.size());
	}else{
		greetings = g;
	}
	//Orders it so that newest post is on top
	Collections.reverse(greetings);
    
    if (greetings.isEmpty()) {
        %>
        <p>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.</p>
        <%
    } 
    else {
        %>
        
        <p>Guestbook history: '${fn:escapeXml(guestbookName)}'.</p>
        <%
        
        // got can't convert from Greeting to Entity when using provided code
		//int post_count = 5;
        //for (int i = 0; i < greetings.size(); i--) {
        for(Greeting greeting: greetings){
        	//if(post_count == 0){
        	//	break;
        	//}
			//Greeting greeting = greetings.get(i);
            pageContext.setAttribute("greeting_content", greeting.getContent());
            
          //Get the date attribute
            pageContext.setAttribute("greeting_date",greeting.getDate().toString());
            
            //if (greeting.getUser() == null) {
                %>
                
                
                
                <!--  BLOG ENTRY ANON 
                <div class = "w3-card-4 w3-margin w3-white">
                <div class = "w3-container">
                <p>An anonymous person wrote:</p>
                </div></div>
                
                -->
                <%
            //} 
            //else {
            	pageContext.setAttribute("greeting_title", greeting.getTitle());
                pageContext.setAttribute("greeting_user", greeting.getUser());
                %>
                
                <!-- BLOG ENTRY USER -->
                
                <div class = "w3-card-4 w3-margin w3-white w3-animate-opacity"> 
                <div class = "w3-container">
                <!-- Print title -->
                <h1>${fn:escapeXml(greeting_title)}</h1>
                <!-- Print the date-->
                <p>on <i>${fn:escapeXml(greeting_date)}</i></p>
                <!-- Print username -->
                <p><b>${fn:escapeXml(greeting_user.nickname)}</b> wrote:</p>
                <!-- Print content -->
                <div class = "w3-row w3-margin">${fn:escapeXml(greeting_content)}</div>
                </div></div>
                
                
                <%
            //}
            //post_count--;
        }
    }
%>

 
	
    <div>
    <a href="guestbook.jsp">History</a>
	<p><a href="<%= userService.createLogoutURL(request.getRequestURI()) %>" class="button">Log out</a></p>
	</div>

	<!-- FOOTER -->
	<footer class = "w3-container w3-dark-grey w3-padding-10 w3-margin-top"><p>Powered by stress and <a href = "https://www.w3schools.com/w3css/default.asp" target = "_blank">w3.css</a></p></footer>

  </body>
</html>