<html>
<head>
    <title>DHCP Leases</title>
    <!-- Favicon, very important -->
    <link rel="icon" href="/favicon.png" type="image/png"/>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>

<body>
<div class="container">
    <h1>DHCP Leases</h1>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Expires</th>
            <th>Mac Address</th>
            <th>Vendor</th>
            <th>Ip Address</th>
            <th>Hostname</th>
        <tr>
        </thead>
        <tbody>
        <?php
        $dhcpLeasesFile = "/var/lib/misc/dnsmasq.leases";

        $dhcpLeases = fopen($dhcpLeasesFile, "r") or die("Unable to open file!");
        if ($dhcpLeases) {
            $arrayLeases = array_filter(explode("\n", fread($dhcpLeases, filesize($dhcpLeasesFile))));
            sort($arrayLeases);
            foreach ($arrayLeases as $arrayLines) {
                $arrayValues = explode(" ", $arrayLines);
                echo "<tr>";
                echo "<td>" . date('d/m/Y H:i:s', $arrayValues[0]) . "</td>";
                echo "<td>" . $arrayValues[1] . "</td>";
                echo "<td>" . getMacAddressVendor($arrayValues[1]) . "</td>";
                echo "<td>" . $arrayValues[2] . "</td>";
                echo "<td>" . $arrayValues[3] . "</td>";
                echo "</tr>";
            }
            fclose($dhcpLeases);
        }

        function getMacAddressVendor($macAddress)
        {
            $url = "http://api.macvendors.com/" . urlencode($macAddress);
            $curl_resource = curl_init();
            curl_setopt($curl_resource, CURLOPT_URL, $url);
            curl_setopt($curl_resource, CURLOPT_RETURNTRANSFER, 1);
            $response = curl_exec($curl_resource);
            if ($response) {
                return $response;
            }
            return "N/A";
        }


        ?>
    </table>
</div>
<hr>
</body>
</html>
