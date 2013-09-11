## JBoss CLI
Contains a Groovy JBoss CLI script to setup HTTP/HTTPS ports and install a certificate.

Execution:

    groovy -cp "/home/user/jboss-eap-6.1/bin/client/jboss-cli-client.jar" jboss-as-setup.groovy -n XXXXXX -w XXXXX -a XXXXX -l TLSv1 -f /home/user/aerogear.keystore -t 8080 -s 8443
