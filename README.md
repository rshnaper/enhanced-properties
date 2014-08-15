enhanced-properties
===================

java.util.Properties enhanced!


About
-----
This a work-in-progress project that extends the default `java.util.Properties` implementation and adds
capability of resolving variables within property values.

Having a sample properties file

> property1=This is a ${property2}

> property2=dynamic property


calling `Properties.getProperty("property1")` would yield to **"This is a dynamic property"**