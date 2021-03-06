# wskafka.conf: A single-node Flume configuration
````properties
# to read data from webserver logs and publish
# to kafka topic

# Name the components on this agent
wk.sources = ws
wk.sinks = kafka
wk.channels = mem

# Describe/configure the source
wk.sources.ws.type = exec
wk.sources.ws.command = tail -F /opt/gen_logs/logs/access.log

# Describe the sink
wk.sinks.kafka.type = org.apache.flume.sink.kafka.KafkaSink
wk.sinks.kafka.brokerList = quickstart.cloudera:9092
wk.sinks.kafka.topic = kafka_demo

# Use a channel wkich buffers events in memory
wk.channels.mem.type = memory
wk.channels.mem.capacity = 1000
wk.channels.mem.transactionCapacity = 100

# Bind the source and sink to the channel
wk.sources.ws.channels = mem
wk.sinks.kafka.channel = mem
````
````bash
$ flume-ng agent --name wk --conf-file /home/cloudera/flume_demo/flume_kafka_sink.conf

$ kafka-console-consumer --bootstrap-server quickstart.cloudera:9092 --topic kafka_demo --from-beginning
````

