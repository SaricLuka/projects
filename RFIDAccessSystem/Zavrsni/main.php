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
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel = "stylesheet" href = "izgled-css/izgled.css"> 
    <link rel = "stylesheet" href = "izgled-css/navigation.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administracija - Main</title>
</head>
<body>
    <ul>
        <li><img class="slika" src="slike/admin.png" alt="admin"><li>
        <li><a class="active">Početna</a></li>
        <li><a href="zapisnik.php">Zapisi posjeta</a></li>
        <li><a href="pregled.php">Izmjena podataka baze</a></li>
        <li><a href="tehnickapomoc.php">Tehnička pomoć</a></li>
        <li><a href="logout.php">Odjava</a></li>
    </ul>
    <div class="div1">
        <?php
            echo "<h1 class='dobro'>Dobrodošli u administraciju baze podataka " . $username . "!</h1>"
        ?>
        <h3 class="podnaslov">Administracija RFID čitača i baze podataka:</h3>
        <p><b>Zapisi posjeta</b> - <i>Zapisi korištenja kartica na RFID čitaču.</i><br>
        <b>Izmjena baze podataka</b> - <i>Brisanje, dodavanje i pregled korisnika iz baze podataka.</i><br>
        <b>Tehnička pomoć</b> - <i>Kontakt kreatora za tehničku pomoć i ostala pitanja.</i><br>
        <b>Tehnička pomoć</b> - <i>Odjava iz administracije</i><br></p>
    </div>
</body>
</html>