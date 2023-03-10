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
    <link rel = "stylesheet" href = "izgled-css/gumb.css">
    <link rel = "stylesheet" href = "izgled-css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upis korisnika</title>
</head>
<body>
    <ul>
        <li><img class="slika" src="slike/admin.png" alt="admin"><li>
        <li><a href="main.php">Početna</a></li>
        <li><a href="zapisnik.php">Zapisi posjeta</a></li>
        <li><a class="active">Izmjena podataka baze</a></li>
        <li><a href="tehnickapomoc.php">Tehnička pomoć</a></li>
        <li><a href="logout.php">Odjava</a></li>
    </ul>
    <div id = "frm"> 
    <?php
        $ime = $_POST['Ime'];
        $prezime = $_POST['Prezime'];
        $email = $_POST['Email'];
        $ib = $_GET['ib'];
        include('connection.php');
        if (!$con) {
            die("Connection failed: " . mysqli_connect_error());
        }
        $sql = "UPDATE Osobe SET Ime='$ime', Prezime='$prezime', Email='$email' WHERE IB ='$ib'";
        if (mysqli_query($con, $sql)) {
            echo "Uspiješno promjenjeni podaci.";
        } else {
            echo "Error: " . $sql . "<br>" . mysqli_error($con);
        }
          
        mysqli_close($con);
    ?>
    <br><br><a class="gumb" href="pregled.php">Natrag</a>
    </div>
</body>
</html>