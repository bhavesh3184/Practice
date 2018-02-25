package com.bavesh.sparkstreaming;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Utilities {

    public static class LocalUtilities {

        public Logger SetLogging()
        {
            Logger logger = LogManager.getLogger("Spark Application");
            logger.setLevel(Level.ERROR);
            return logger;
        }


    }

}
