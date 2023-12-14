# Whisper Chat

A lightweight, loggless server with a client that comes with adversarial stylometry
plugins.

## Server local testing instructions
1. Install [deno](https://docs.deno.com/runtime/manual/getting_started/installation)
   and [mkcert](https://github.com/FiloSottile/mkcert)
3. `deno task localcert`
4. Create a `.env` file in the server directory. Set `KEY_PATH` and `CERT_PATH`.
6. `deno task start`

## Client build instructions
1. `mvn compile`
2. `mvn package`
3. For more detailed information on building the plugins, visit the
   plugins directory README
