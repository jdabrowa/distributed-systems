package pl.jdabrowa.agh.distributed.ice.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IceConfigurator {

    public static final String ICE_CONFIGURATION_PROPERTY_KEY = "Ice.Config";

    public String [] addConfiguration(String [] args, String value) {
        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        boolean propertyPresent = false;
        String regex = String.format("--%s=.*?", ICE_CONFIGURATION_PROPERTY_KEY);
        Pattern propertyWithKeyPattern = Pattern.compile(regex);
        for(String s : args) {
            Matcher matcher = propertyWithKeyPattern.matcher(s);
            if(matcher.matches()){
                propertyPresent = true;
                break;
            }
        }
        if(propertyPresent) {
            return args;
        } else {
            String propertyArg = String.format("--%s=%s", ICE_CONFIGURATION_PROPERTY_KEY, value);
            argsList.add(propertyArg);
            return argsList.toArray(new String[argsList.size()]);
        }
    }
}
