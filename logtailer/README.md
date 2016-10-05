# Log Tailer

This module is intended to be launched on the init stage during boot to monitor the dnsmasq.log file.

## ToDo

* Skip reading content already on file and post only new content
* Implement Http connector, should be on a separate thread to avoid blocking LogTailerListener