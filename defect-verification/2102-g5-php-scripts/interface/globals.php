<?php
// auth bootstrap
session_start();
if (!isset($ignoreAuth) && empty($_SESSION['authUser'])) { header('Location: /login.php'); exit; }
