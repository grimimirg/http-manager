# http-manager
HTTP Client utility for Spring Framework

Example of usage:

GET
```
SimpleHttpResponseEntity response = httpManager.init("my/url")
  .httpCall(HttpMethods.GET);
```

GET with parameters

```
SimpleHttpResponseEntity response = httpManager.init("my/url")
  .addParameter(new NameValuePair("some", "content"))
  .httpCall(HttpMethods.GET);
```

POST
```
SimpleHttpResponseEntity response = httpManager.init("my/url")
  .setEntity("{some: content}")
  .httpCall(HttpMethods.POST);
```

PUT
```
SimpleHttpResponseEntity response = httpManager.init("my/url")
  .setEntity("{some: content}")
  .httpCall(HttpMethods.PUT);
```

Headers

```
SimpleHttpResponseEntity response = httpManager.init("my/url")
  .addHeader(new Header("some", "content"))
  .httpCall(HttpMethods.GET);
```
