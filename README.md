# Chatbox
## The program is deployed on Amazon Web Service E2

This chatbox is designed for multi-user chat.
The program is mainly written in Java.
Socket programming as well as Javafx techniques are practiced.
<br/>
## Activate the server/client jar file
Type these commands on terminal after dragging these files on laptop
```java
java -jar Client.jar
java -jar Server.jar
```

## Socket Programming to connect the users
```Java
Scanner dis= new Scanner(socket.getInputStream());
output = new PrintWriter(socket.getOutputStream(), true);
```

base64 Image conversion so that other users can get the value of the image through the socket.
```Java
base64Image = Base64.getEncoder().encodeToString(imageData);
```

## First sign in with a name
<div style="text-align:center"><image src="https://github.com/issacto/Chatbox/blob/master/Images/Screenshot%202020-07-28%20at%206.46.17%20PM.png" width =400 ><div/>

<br/>
## Then change profile picture and chat@-@
<image src="https://github.com/issacto/Chatbox/blob/master/Images/Screenshot%202020-07-28%20at%206.44.11%20PM.png" width=800>
