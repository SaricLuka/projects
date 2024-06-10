<?php
session_start();
include('connection.php');

$username = $_POST['user'];
$password = $_POST['pass'];

$sql = "SELECT password FROM admin WHERE username = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();
$row = $result->fetch_assoc();
$count = $result->num_rows;

if ($count === 1) {
    if (password_verify($password, $row["password"])) {
        $_SESSION["username"] = $username;
        Redirect('main.php', false);
    } else {
        header("refresh:3;url=index.php");
        echo "<h1>Prijava neuspiješna. Korisničko ime ili lozinka su neispravni. Preusmjeravam...</h1>";
    }
} else {
    header("refresh:3;url=index.php");
    echo "<h1>Prijava neuspiješna. Korisnik ne postoji. Preusmjeravam...</h1>";
}

function Redirect($url, $permanent = false)
{
    header('Location: ' . $url, true, $permanent ? 301 : 302);
    exit();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
    <link rel="stylesheet" href="izgled-css/izgled.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prijava</title>
</head>
<body>

</body>
</html>
