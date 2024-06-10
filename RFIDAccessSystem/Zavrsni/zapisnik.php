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
    <link rel = "stylesheet" href = "izgled-css/tablica.css"> 
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Zapisnik posjeta</title>
</head>

<body>
    <ul>
        <li><img class="slika" src="slike/admin.png" alt="admin"><li>
        <li><a href="main.php">Početna</a></li>
        <li><a class="active">Zapisi posjeta</a></li>
        <li><a href="pregled.php">Izmjena podataka baze</a></li>
        <li><a href="tehnickapomoc.php">Tehnička pomoć</a></li>
        <li><a href="logout.php">Odjava</a></li>
    </ul>
    <h1 class="naslov">Zapisnik</h1>

    <table>
        <tr>
            <th>Vrijeme</th>
            <th>Email</th>
            <th>Ime</th>
            <th>Prezime</th>
        </tr>

<?php
    include('connection.php');  
    $sql = "SELECT * FROM zapisnik INNER JOIN osobe ON zapisnik.IB = osobe.IB;";
    $result = mysqli_query($con, $sql);
    if (mysqli_num_rows($result) == 0) {
        die("Sending the query failed! ");
    }
    while ($row = mysqli_fetch_array($result)) {
        echo "<div>";
        echo "<tr>";
        echo "<td class=\"ime\">" . $row['Vrijeme'] . "</td>";
        echo "<td class=\"ime\">" . $row['Email'] . "</td>";
        echo "<td class=\"ime\">" . $row['Ime'] . "</td>";
        echo "<td class=\"ime\">" . $row['Prezime'] . "</td>";
        echo "</tr>";
        echo "</div>";
    }   
    mysqli_close($con);
?>
    </table>
</body>
</html>