<html xmlns:g="http://www.w3.org/1999/html">
<head>
    <title>Upload Image</title>
    <meta name="layout" content="main"/>
</head>
<body>
    <h1>Upload an Image</h1>
    <g:uploadForm action="upload">
        <table>
            <tr>
                <td>User Id:</td>
                <td><g:select name="loginId" from="${userList}" optionKey="loginId"
                          optionValue="loginId"/></td>
            </tr>
            <tr>
                <td>Photo:</td> <td><input name="photo" type="file" />
                <g:submitButton name="upload" value="Upload"/></td>
            </tr>
        </table>
    </g:uploadForm>
</body>
</html>