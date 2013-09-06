## JBoss CLI scripts
Contains a Groovy JBoss CLI script to setup HTTP/HTTPS ports and install a certificate.

Execution:

    groovy -cp "/home/user/Downloads/jboss-eap-6.1/bin/client/jboss-cli-client.jar" jboss-as-setup.groovy -keyName=aerogear -keyPassword=XXXX -keyAlias=aerogear -keyProtocol=TLSv1 -keyFile=/home/user/XXXX.keystore -httpPort=8080 -httpsPort=8444 - managementHost=127.0.0.1 -managementPort=9999
