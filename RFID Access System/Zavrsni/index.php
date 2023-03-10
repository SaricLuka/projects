<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Prijava</title>
	<link rel = "stylesheet" href = "izgled-css/style.css">
    <link rel = "stylesheet" href = "izgled-css/gumb.css">
</head>
<body>
    <div id = "frm">  
        <h1>Prijava</h1>  
        <form name="form1" action = "login.php" onsubmit = "return validation()" method = "POST">  
            <p>  
                <label>Korisničko ime:</label>  
                <input type = "text" id ="user" name  = "user" />  
            </p>  
            <p>  
                <label>Lozinka:</label>  
                <input type = "password" id ="pass" name  = "pass" />  
            </p>  
            <p>     
                <input type = "submit" class="gumb" value = "Login" />  
            </p>  
        </form>  
    </div>  
    <script>  
            function validation()  
            {  
                var id=document.form1.user.value;  
                var ps=document.form1.pass.value;  
                if(id.length=="" && ps.length=="") {  
                    alert("Korisničko ime i lozinka su prazni.");  
                    return false;  
                }  
                else  
                {  
                    if(id.length=="") {  
                        alert("Korisničko ime je prazno.");  
                        return false;  
                    }   
                    if (ps.length=="") {  
                        alert("Lozinka je prazna.");  
                        return false;  
                    }  
                }                             
            }  
        </script>  
</form>
</body>
</html>