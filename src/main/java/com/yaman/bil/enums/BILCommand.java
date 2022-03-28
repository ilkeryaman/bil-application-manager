package com.yaman.bil.enums;

public enum BILCommand {
    START("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/start.sh'"),
    SHUT_DOWN("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/shutdown.sh'"),
    FORCE_STOP("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/jboss-cli.sh --connect --controller=localhost:9990 command=:shutdown'"),
    CHECK("ps -ef | grep bil*.*jboss"),
    GET_LAST_LOG_TIME("egrep -ir \"20[0-9]{2}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} \" /opt/{bil.instance.name}/wildfly-8.1.0.Final/standalone/log/server.log | tail -n 1 | cut -c1-23"),
    GET_STARTED_LOG("egrep -ir \"WildFly 8.1.0.Final \\\"Kenny\\\" started in [0-9]+ms\" /opt/{bil.instance.name}/wildfly-8.1.0.Final/standalone/log/server.log | tail -n 1");

    private String command;

    BILCommand(String command){
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command){
        this.command = command;
    }
}
