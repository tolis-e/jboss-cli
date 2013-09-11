## JBoss CLI
Contains a Groovy JBoss CLI script to setup HTTP/HTTPS ports and install a certificate.

Execution:

    groovy -cp "/home/user/jboss-eap-6.1/bin/client/jboss-cli-client.jar" jboss-as-setup.groovy -n XXXXXX -w XXXXX -a XXXXX -l TLSv1 -f /home/user/aerogear.keystore -t 8080 -s 8443

    usage: groovy -cp "./jboss-cli-client.jar" jboss-as-setup.groovy
    -a,--keyStoreAlias <keyAlias>             setup the key alias
    -f,--keyStoreFile <keyFile>               setup the keystore file
    -h,--help                                 Help - Usage Information
    -l,--SSL protocol <sslProtocol>           setup the SSL protocol
    -m,--managementHost <managementHost>      management host for establishing connection
    -n,--keyStoreName <keyName>               setup the ssl element name
    -p,--managementPort <managementPort>      management port for establishing connection
    -s,--httpsPort <httpsPort>                setup the HTTPS port
    -t,--httpPort <httpPort>                  setup the HTTP port
    -w,--keyStorePassphrase <keyPassphrase>   setup the key password

