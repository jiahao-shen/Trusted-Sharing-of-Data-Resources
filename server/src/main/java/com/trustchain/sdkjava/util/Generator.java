package com.trustchain.sdkjava.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static String userID(int len) {
        List<CharacterRule> rules = new ArrayList<>();
        rules.add(new CharacterRule(EnglishCharacterData.Alphabetical));
        rules.add(new CharacterRule(EnglishCharacterData.Digit));

        PasswordGenerator pg = new PasswordGenerator();
        return pg.generatePassword(len, rules);
    }

    public static String password(int len) {
        List<CharacterRule> rules = new ArrayList<>();
        rules.add(new CharacterRule(EnglishCharacterData.Alphabetical));
        rules.add(new CharacterRule(EnglishCharacterData.Digit));

        PasswordGenerator pg = new PasswordGenerator();
        return pg.generatePassword(len, rules);
    }
}
