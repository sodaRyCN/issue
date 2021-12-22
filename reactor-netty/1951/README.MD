1.run ServiceBootStartup.

2.wait ServiceBootStartup running, run Gateway

3.wait Gateway running, run Client

- ServiceBootStartup is gRPC server port 8081. if u want to change port:
    - modify ServiceBootStartup.java line 21 and resource/application.yaml line 14.
- Client is gRPC client.
- Gateway is spring cloud gateway port 5530, it contains httpclient config. And if u want to change port:
    - modify Client.java line 9 and resource/application.yaml line 2.
    
if modify Gateway.java line 109, set httpclient protocol only h2c, we will get the reply "demo success" (it's defined in DemoServiceImpl),
and change protocol to h2c and http1, we will get an exception.


