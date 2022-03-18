package com.yaman.bil.enums;

public enum BILCommand {
    START("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/start.sh'"),
    SHUT_DOWN("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/shutdown.sh'"),
    FORCE_STOP("sh -c 'export WILDFLY_HOME=/opt/{bil.instance.name}/wildfly-8.1.0.Final; export BIL_HOME=/opt/{bil.instance.name}; /opt/{bil.instance.name}/wildfly-8.1.0.Final/bin/jboss-cli.sh --connect --controller=localhost:9990 command=:shutdown'"),
    CHECK("ps -ef | grep bil*.*jboss");

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
