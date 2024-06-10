<?php
    session_start();
    if( ! isset($_SESSION["username"]) ){  
        die('Samo prijavljeni korisnici mogu pristupiti ovoj stranici. Preusmjeravam...');
        header("refresh:5;url=index.php");
    }
    else{
        $username=$_SESSION["username"];
    session_destroy();
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel = "stylesheet" href = "izgled-css/izgled.css">
    <link rel = "stylesheet" href = "izgled-css/style.css">
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Odjava</title>
</head>
<body>
<div id = "frm"> 
<?php
    header("refresh:3;url=index.php");
    echo("Uspiješno odjavljeno, preusmjeravam...");
?>
</div>
</body>
</html>