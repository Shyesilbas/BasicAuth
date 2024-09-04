Jwt operations and Authentication and bank account
For Customer role : username = customer , password = customer123 ( AuthController /auth/login )
Set your database at app.properties
Login with given data
after Logging in , you will receive a token. Token expires after 10 hours 
you have to enter this token to do any process ( to check Authorization )
Do your requests at postman , easy to handle token entry , choose Bearer token from Authorization
after you are done , find logout endpoint at AuthController to Expire the token you logged in.
After the logout , the token will be added to black list token database , And you cannot do any process until you login again
You always need to enter your active token to do process
