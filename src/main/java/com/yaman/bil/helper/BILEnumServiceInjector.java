package com.yaman.bil.helper;

import com.yaman.bil.enums.BILCommand;
import com.yaman.bil.enums.BILCommandResponse;
import com.yaman.property.IPropertyManager;
import com.yaman.property.enums.BILInstanceProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;

@Component
public class BILEnumServiceInjector {
    @Autowired
    private IPropertyManager propertyManager;

    @PostConstruct
    public void postConstruct() {
        for (BILCommand bilCommand : EnumSet.allOf(BILCommand.class)) {
            bilCommand.setCommand(
                    bilCommand.getCommand().replace(
                            "{bil.instance.name}",
                            propertyManager.getProperties().getProperty(BILInstanceProperty.NAME.getKey())));
        }

        for (BILCommandResponse bilCommandResponse : EnumSet.allOf(BILCommandResponse.class)) {
            bilCommandResponse.setValue(
                    bilCommandResponse.getValue().replace(
                            "{bil.instance.name}",
                            propertyManager.getProperties().getProperty(BILInstanceProperty.NAME.getKey())));
        }
    }
}