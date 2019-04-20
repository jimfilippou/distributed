### A multi-threaded, distributed publish-subscribe system

#### Diagram

![](Skeleton.png)

#### Running entities

This requires 3 processes, compile the project with IDEA and after opening 3 terminal 
sessions, fist run publishers, then brokers and finally consumers. The executable
class files are located at `out/production/distributed`. You will have to edit
the IP of the main class, also edit `data/brokers.txt` to match your local network **and** 
finally edit `helpers` package to use a proper absolute path.

##### Brokers
`java Main brokers -p 192.168.1.4:9090`

##### Publisher
`java Main publishers`

##### Consumer
`java Main consumers`