<?php
    session_start();
    if( ! isset($_SESSION["username"]) ){  
        header("refresh:5;url=index.php");
        die('Samo prijavljeni korisnici mogu pristupiti ovoj stranici. Preusmjeravam...');
    }
    else{
        $username=$_SESSION["username"];
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
    <link rel = "stylesheet" href = "izgled-css/izgled.css"> 
    <link rel = "stylesheet" href = "izgled-css/navigation.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tehnička pomoć</title>
</head>
<body>
    <ul>
        <li><img class="slika" src="slike/admin.png" alt="admin"><li>
        <li><a href="main.php">Početna</a></li>
        <li><a href="zapisnik.php">Zapisi posjeta</a></li>
        <li><a href="pregled.php">Izmjena podataka baze</a></li>
        <li><a class="active">Tehnička pomoć</a></li>
        <li><a href="logout.php">Odjava</a></li>
    </ul>
    <div class="div1">
        <h1 class="dobro">Tehnička pomoć i pitanja</h1>

        <h3>Kontakt:</h3><br><br>
        <p><b>Broj telefona: </b>069 999 420</p><br>
        <p><b>Email: </b>saricluka76@email.com</p><br><br><br>

        <h3>Info:</h3><br><br>
        <p><b>Također posjetite: </b><a href="https://www.youtube.com/channel/UC8wP1YPngeyvG7-3vtEhr9A">Ovo je kul ; )</a></p><br><br>
        <div><a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ"><img class="slika2" src="slike/chonker.jpg"></a></div>
    </div>

</body>
</html>
