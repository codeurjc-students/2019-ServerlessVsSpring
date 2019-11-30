# REST comparative AWS Lambda and Spring with Java

## To get started

<details>
<summary>AWS Lambda</summary>
<p>

## Requirements

For REST-Lambda demo is necessary:

- [AWS account:](https://aws.amazon.com/) Choose Create an AWS Account, or Complete Sign Up.
- [AWS CLI:](https://docs.aws.amazon.com/es_es/cli/latest/userguide/cli-chap-install.html) The AWS Command Line Interface (AWS CLI) is an open source tool that enables you to interact with AWS services using commands in your command-line shell.
- [AWS SAM CLI:](https://aws.amazon.com/es/serverless/sam/) This is an AWS CLI tool that helps you develop, test, and analyze your serverless applications locally.
- [Maven.](https://maven.apache.org/download.cgi)
- [Java JDK.](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Configure AWS CLI

1. In AWS Web, click in your user > My Security Credentials:

    ![AWS CLI Credential](./images/cli-credential.png)

2. In console, write "aws configure" command and add the id and secret of aws web, select your region and preferred output format. If you want to create a specific configuration profile use: "aws configure --profile <profileName\>":

    ![AWS configure](./images/aws-configure.png)

## Installation

1. Write the following command to clone this repository in the dir that you want:

    ``` sh
    git clone https://github.com/codeurjc-students/2019-ServerlessVsSpring.git
    ```

2. From the console, navigate to the folder **"sections/REST-AWS-Spring/source/aws-lambda/HelloWorldFunction"**.

3. To install the necessary dependencies for this project, execute:
    ``` sh
    mvn clean install
    ```

4. Create an AWS S3 bucket to storage the application.

    To create the bucket, use this command:
    ``` sh
    aws s3api create-bucket --bucket rest-demo-serverless-vs-spring --region eu-west-1 --create-bucket-configuration LocationConstraint=eu-west-1
    ```

5. We need to package our SAM applicaction. Execute this command in **aws-lambda** folder:
    ``` sh
    sam package --template-file template.yaml --s3-bucket my-bucket --output-template-file packaged-template.yaml
    ```

6. Now we must deploy the application with the following command:
    ``` sh
    sam deploy --template-file packaged-template.yaml --stack-name rest-demo-serverless-vs-spring --capabilities CAPABILITY_IAM
    ```

## Use

We can get the url of the lambda function by navigating to API Gateway:

![API Gateway](./images/api.png)


</details>
</p>
<details>
<summary>Spring with Java</summary>
<p>

## Requirements

For REST-Spring demo is necessary:

- [Maven.](https://maven.apache.org/download.cgi)
- [Java JDK.](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Installation

1. Write the following command to clone this repository in the dir that you want:
    ``` sh
    git clone https://github.com/codeurjc-students/2019-ServerlessVsSpring.git
    ```

2. From the console, navigate to the folder **"sections/REST-AWS-Spring/source/java-spring/hello_world"**.

3. To install the necessary dependencies for this project, execute:
    ``` sh
    mvn clean install
    ```

## Use

1. Run the application:
    ``` sh
    java -jar target/hello_world-0.0.1-SNAPSHOT.jar
    ```
    or 
    ``` sh
    mvn spring-boot:run
    ```

2. View get response in localhost: http://localhost:8080/greeting
   
</details>
</p>

## Comparative

### Functionality

About the functionality there are no differences, both, Spring and lambda, receive a request and execute a code that elaborates the response. If we focus on the number of requests that can be processed, the winner is undoubtedly AWS Lambda, as it scales according to the number of requests to respond. In Spring, an autoescalated system is not contemplated, an architecture that responds according to the number of requests must be designed, for the other hand, with lambda, that architecture is implicit.

### Implementation

With respect to the implementation, Spring presents a clearer and more defined separation of objects, however, the objects are comparable in our examples:

Spring presents a main function clearly separated from the rest of the code:

**[Application.java](./source/java-spring/hello_world/src/main/java/hello/Application.java) in Spring:**
``` java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

The closest thing to a main entry with respect to Lambda is the template we generate with SAM, which indicates which controller should be executed when calling lambda (Resources.HelloWorldFunction.Properties.Handler):

**[template.yaml](./source/aws-lambda/template.yaml) in AWS Lambda:**
``` yaml
Resources:
  HelloWorldFunction:
    Type: AWS::Serverless::Function
    Properties:
      CodeUri: HelloWorldFunction
      Handler: helloworld.App::handleRequest 
      Runtime: java8
      MemorySize: 512
      Environment:
        Variables:
          PARAM1: VALUE
      Events:
        HelloWorld:
          Type: Api
          Properties:
            Path: /hello
            Method: get
```
We also see that the template sets the values of the lambda function, which will be of the Api type (``@RestController`` in Spring), sets the route where it will be executed (the equivalent of ``@RequestMapping ("/ hello")`` in Spring) and with what method (In Spring, the get method is implicit if it is a function and not a method that handles the request).

**[GreetingController.java](./source/java-spring/hello_world/src/main/java/hello/GreetingController.java) in Spring:**
``` java
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
```
As we have seen in the template handler, the request will call the function **HandleRequest** of the class **App**:

**[App.java](./source/aws-lambda/HelloWorldFunction/src/main/java/helloworld/App.java) in AWS Lambda:**
``` java
public class App implements RequestHandler<Object, Object> {

    public Object handleRequest(final Object input, final Context context) { 
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);
            return new GatewayResponse(output, headers, 200);
        } catch (IOException e) {
            return new GatewayResponse("{}", headers, 500);
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
```
The handle prepares the elements that the GatewayResponse object needs. 
``final Object input`` is the equivalent of ``@RequestParam (value="input") Object input)``
The objects **Greeting** and **GatewayResponse** are responsible for responding to the GET request of each example.

<details>
<summary>Greeting.java class in Spring</summary>
<p>

[Greeting.java](./source/java-spring/hello_world/src/main/java/hello/Greeting.java)

``` java
public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
```
</details>
</p>

<details>
<summary>GatewayResponse.java class in AWS Lambda</summary>
<p>

[GatewayResponse.java](./source/aws-lambda/HelloWorldFunction/src/main/java/helloworld/GatewayResponse.java)

``` java
public class GatewayResponse {

    private final String body;
    private final Map<String, String> headers;
    private final int statusCode;

    public GatewayResponse(final String body, final Map<String, String> headers, final int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
```
</details>
</p>

The way to implement each of the two options also differs as you can see the point “To get started”.

