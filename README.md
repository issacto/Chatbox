## Chatbox
# Thr program is deployed on Amazon Web Service E2
This chatbox is designed for multi-user chat.
The program is mainly written in Java.
Socket programming as well as Javafx techniques are practiced.
<br>
<br>

Socket Programming to connect the users
```Java
Scanner dis= new Scanner(socket.getInputStream());
output = new PrintWriter(socket.getOutputStream(), true);
```

base64 Image conversion so that other users can get the value of the image through the socket.
```Java
base64Image = Base64.getEncoder().encodeToString(imageData);
```



