# ADR: Choosing a Method for Telegram API Integration in a Spring-based Project

**Date**: 3.11.2024

## Context
The project requires integration with Telegram for tasks such as sending messages, receiving updates, and managing chat interactions. The approach should be compatible with Spring, easy to maintain, and able to support required Telegram functionalities, such as webhooks and polling. The primary options considered are using an SDK (specifically, the popular **TelegramBots SDK** by Ruben Lagus) or making direct REST API calls.

## Alternatives

### 1. TelegramBots SDK
- **Description**: The [TelegramBots SDK](https://github.com/rubenlagus/TelegramBots) is a popular Java SDK for integrating with Telegram’s API, designed to streamline bot management and message handling.
- **Authorization**:
    - Uses Telegram bot tokens directly, simplifying authentication processes within the SDK.
- **Advantages**:
    - Simplifies setup with ready-to-use methods for common tasks like sending messages and managing webhooks.
    - Strong community support and frequent updates ensure reliability and maintainability.
    - Built-in error handling and request management utilities enhance ease of use.
- **Disadvantages**:
    - Dependence on SDK updates, which may affect compatibility with future Telegram API versions.
    - Limited flexibility if the SDK lacks specific functionality needed for advanced customization.

### 2. Direct REST API Calls
- **Description**: Integrating with Telegram’s REST API directly using Spring’s `RestTemplate` or `WebClient`.
- **Authorization**:
    - Relies on bot tokens for authentication, similar to SDK usage.
- **Advantages**:
    - Offers full flexibility and control over request handling, allowing fine-tuned customizations.
    - Reduces dependency on external libraries, relying only on Spring’s built-in REST support.
- **Disadvantages**:
    - Requires additional code for constructing requests, managing errors, and parsing responses.
    - Higher development overhead and maintenance costs, especially for error handling and webhook management.

## Decision
After evaluation, **TelegramBots SDK** by Ruben Lagus is recommended as the primary integration method for this project.

## Rationale
- The **TelegramBots SDK** provides ease of implementation, with robust built-in support for Telegram functionalities like webhooks, error handling, and polling. This allows the project to save development time and simplify maintenance by using the SDK’s pre-built features.
- Direct REST API calls provide greater flexibility but require significantly more setup and maintenance, making them less optimal for the current project scope.

## Chosen Option
Use **TelegramBots SDK** as the primary method for Telegram API integration.

## Additional Documentation Links
- [TelegramBots SDK GitHub Repository](https://github.com/rubenlagus/TelegramBots)
- [Telegram REST API Documentation](https://core.telegram.org/bots/api)
