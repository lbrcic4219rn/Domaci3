# mini-java-http-framework

Minimal HTTP server implementation in Java featuring custom request parsing, controller-based routing, in-memory storage, and a separate quote-of-the-day service communicating via raw sockets.

## Features
- Manual HTTP request line/header parsing over `Socket`/`ServerSocket` (no Servlet API, no framework).
- A reusable `http` core: one `Server` (accept loop over a fixed thread pool) and one `ServerThread` (parse → handle → respond), with applications plugging in through a single `RequestHandler` interface. The core has no knowledge of any application.
- `Response` hierarchy covering HTML, JSON, redirects and plain-text errors, with `Content-Length` computed in UTF-8 bytes and a hook for extra headers.
- Failure modes answer rather than hang up: unmatched route → 404, unsupported method → 501, malformed request line → 400, handler blowing up → a logged 500.
- Ports, host, timeout and pool size configurable by system property or environment variable (see [Configuration](#configuration)).
- Two applications on the same core: a JSON quote-of-the-day service (`GET /`) and an HTML quote board (`GET /quotes`, `POST /save-quote`) that fetches the quote of the day from the service over a raw socket.
- In-memory storage only — submitted quotes reset on restart.

## Project Structure
```
src/
├── http/                                  # the framework — no application knowledge
│   ├── Server.java                        # accept loop + thread pool, takes a RequestHandler
│   ├── ServerThread.java                  # parses a Request, writes a Response
│   ├── RequestHandler.java                # the seam applications implement
│   ├── {Request,HttpMethod}.java
│   └── response/{Response,PlainTextResponse,HtmlResponse,JsonResponse,
│                 RedirectResponse,NotFoundResponse,InternalServerErrorResponse}.java
├── config/Config.java                     # system property / env var lookup
└── quotes/
    ├── Quote.java                         # record(author, text) — shared wire contract
    ├── service/                           # JSON quote-of-the-day service, port 8114
    │   ├── Main.java
    │   ├── QuoteServiceHandler.java
    │   └── QuoteStorage.java              # hardcoded seed quotes
    └── board/                             # HTML quote board, port 8113
        ├── Main.java
        ├── QuoteBoardHandler.java         # path/method routing
        ├── QuoteOfTheDayClient.java       # socket client calling the service
        ├── QuotesStorage.java             # in-memory saved quotes
        └── controller/{Controller,QuotesController}.java
```

Dependencies point one way: `quotes.board` and `quotes.service` both depend on `http` and `config`, neither depends on the other, and `http` depends on nothing in the project. The two applications meet only over a socket at runtime.

## Configuration
Every setting resolves in the order **system property → environment variable → default**, via `main.config.Config`. The environment variable is the property key upper-cased with dots replaced by underscores.

| Property | Environment variable | Default | Meaning |
| --- | --- | --- | --- |
| `quotes.board.port` | `QUOTES_BOARD_PORT` | `8113` | Port for the HTML quote board |
| `quotes.service.port` | `QUOTES_SERVICE_PORT` | `8114` | Port for the quote-of-the-day service — read by both the service and the client that calls it |
| `quotes.service.host` | `QUOTES_SERVICE_HOST` | `localhost` | Host the board dials for the quote of the day |
| `quotes.service.timeout.ms` | `QUOTES_SERVICE_TIMEOUT_MS` | `2000` | Connect/read timeout for that call |
| `quotes.pool.size` | `QUOTES_POOL_SIZE` | `10` | Worker threads per server |

Either mechanism works with the Gradle run tasks:

```bash
./gradlew runQuoteBoard -Dquotes.board.port=9113 -Dquotes.service.port=9114
```

An unparseable numeric value logs a warning and falls back to the default rather than failing to start.

The quote service must be running for `GET /quotes` to show a quote of the day; if it's down, the page renders a placeholder instead and the request still returns 200.

## Building and running
Gradle (with the committed wrapper) handles the build and resolves Gson from Maven Central — nothing needs to be installed beyond a JDK 21.

```bash
./gradlew build
```

Start the quote-of-the-day service first, then the quote board, in two terminals:

```bash
./gradlew runQuoteService
```

```bash
./gradlew runQuoteBoard
```

Then open <http://localhost:8113/quotes>.
