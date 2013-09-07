/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jboss.as.cli.scriptsupport.*

httpPort = ''
httpsPort = ''
keyName = ''
keyAlias = ''
keyFile = ''
keyProtocol = ''
keyPassword = ''
managementHost = '127.0.0.1'
managementPort = '9999'
for (i in args) 
{

    argsArr = i.split("=")
    val = argsArr != null && argsArr.length == 2 ? argsArr[1] : ''
    if ((i.startsWith('-managementHost=') || i.startsWith('managementHost=')) && val != '')
    {
        managementHost = val
    }
    if ((i.startsWith('-managementPort=') || i.startsWith('managementPort=')) && val != '')
    {
        managementPort = val
    }
    if (i.startsWith('-httpPort=') || i.startsWith('httpPort='))
    {
        httpPort = val
    }
    else if (i.startsWith('-httpsPort=') || i.startsWith('httpsPort='))
    {
        httpsPort = val
    }
    else if (i.startsWith('-keyName') || i.startsWith('keyName'))
    {
        keyName = val
    }
    else if (i.startsWith('-keyAlias') || i.startsWith('keyAlias'))
    {
        keyAlias = val
    }
    else if (i.startsWith('-keyPassword') || i.startsWith('keyPassword'))
    {
        keyPassword = val
    }
    else if (i.startsWith('-keyFile') || i.startsWith('keyFile'))
    {
        keyFile = val
    }
    else if (i.startsWith('-keyProtocol') || i.startsWith('keyProtocol'))
    {
        keyProtocol = val
    }
    else if (i.startsWith('--help') || i.startsWith('-help'))
    {
        println("USAGE:")
        println("       -managementHost=XXXX   : management host for establishing connection")
        println("       -managementPort=XXXX   : management port for establishing connection")
        println("       -httpPort=XXXX   : setup the HTTP port")
        println("       -httpsPort=XXXX  : setup the HTTPS port")
        println("       -keyName=XXXX    : setup the ssl element name")
        println("       -keyAlias=XXXX   : setup the key alias")
        println("       -keyPassword=XXXX   : setup the key password")
        println("       -keyFile=XXXX    : setup the keystore file")
        println("       -keyProtocol=XXX : setup the SSL protocol")
        System.exit(0)
    }
}

if ((httpPort != '' && !httpPort.isNumber()) || (httpsPort != '' && !httpsPort.isNumber()))
{
    println("Invalid Ports HTTP port: '" + httpPort + "' HTTPS port: '" + httpsPort + "'")
    System.exit(1)
}
else if (keyPassword == '' || keyAlias == '' || keyFile == '' || keyName == '' || keyProtocol == '')
{
    println("Invalid Keystore configuration name: '" + keyName + "' password: '" + keyPassword + "' alias: '" + keyAlias + "' file: '" + keyFile + "' protocol: '" + keyProtocol + "'")
    System.exit(1)
}
else
{
    cli = CLI.newInstance()
    exception = null
    try {
        cli.connect(managementHost + ":" + managementPort)

        if (httpPort != '')
        {
            cli.cmd("cd socket-binding-group=standard-sockets/socket-binding=http")
            println("Changing HTTP port:")
            result = cli.cmd(":write-attribute(name=port,value=" + httpPort + ")")
            cli.cmd("cd ../..")
            response = result.getResponse()
            println(response)
        }
    
        if (httpsPort != '')
        {
            cli.cmd("cd socket-binding-group=standard-sockets/socket-binding=https")
            println("Changing HTTPS port:")
            result = cli.cmd(":write-attribute(name=port,value=" + httpsPort + ")")
            cli.cmd("cd ../..")
            response = result.getResponse()
            println(response)
        }

        if (keyName != '' && keyAlias != '' && keyPassword != '' && keyFile != '' && keyProtocol != '')
        {
            cli.cmd("cd subsystem=web/connector=https")

            println("Checking if HTTPS connector already exists:")
            result = cli.cmd(":read-attribute(name=name)")
            response = result.getResponse()
            println(response)
            if (!response.asString().contains("undefined"))
            {
                println("Removing existing HTTPS connector:")
                result = cli.cmd(":remove")
                response = result.getResponse()
                println(response)
            }
            println("Response from creating https connector:")
            result = cli.cmd(":add(name=\"https\", protocol=\"HTTP/1.1\", scheme=\"https\", socket-binding=\"https\")")
            response = result.getResponse()
            println(response)
         
            println("Response from creating ssl element:")
            result = cli.cmd("./ssl=configuration:add(name=\"" + keyName + "\", key-alias=\"" + keyAlias + "\", password=\"" + keyPassword + "\", certificate-key-file=\"" + keyFile + "\", protocol=\"" + keyProtocol + "\")")
            response = result.getResponse()
            println(response)

            cli.cmd("cd ../..")
        }
 
        reloadCmd = ":reload"

        result = cli.cmd(reloadCmd)
        response = result.getResponse()
        println(response)

    } catch (Exception ex) {
        println(ex)
        exception = ex
    } finally { 
        try { cli.disconnect() } catch (Exception ignore) {}
        System.exit(exception == null ? 0 : 1)        
    }
}
