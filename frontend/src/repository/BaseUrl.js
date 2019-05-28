const baseUrl = window.location.hostname.indexOf("localhost") != -1 ? "http://localhost:9000" : window.location.protocol + "//" + window.location.hostname + ":" + window.location.port

export default baseUrl