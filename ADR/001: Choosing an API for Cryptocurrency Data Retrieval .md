# ADR: Choosing an API for Cryptocurrency Data Retrieval

**Date**: 31.10.2024

## Context
The project requires a stable and free access to cryptocurrency market data. The data must be current and include information on real-time prices. We need to consider APIs that provide data without registration or authorization and with rate limits that allow integration into an application without additional costs. The main alternatives considered are the Binance API and CoinGecko API.

## Alternatives

### 1. Binance API
- **Description**: The Binance API provides detailed access to cryptocurrency market data, including real-time prices.
- **Authorization**:
    - Authorization is **not required** for public market data retrieval.
    - Access to personal account information, such as balances or order history, requires an API key.
- **Rate Limits for Non-Authenticated Users**:
    - Binance API allows **up to 1200 requests per minute** for public data, which is suitable for intensive use.
- **Advantages**:
    - Real-time data support with a high request rate limit for public information.
    - Reliable and stable operation.
- **Disadvantages**:
    - Only provides cryptocurrency data (no traditional financial instruments).

### 2. CoinGecko API
- **Description**: CoinGecko API aggregates data from various cryptocurrency exchanges and provides access to prices, trading volumes, market capitalization, and historical data.
- **Authorization**:
    - **Not required** for data access, making it convenient for free use without the need to create an account.
- **Rate Limits for Non-Authenticated Users**:
    - CoinGecko API allows **up to 50 requests per minute**, sufficient for most analytical projects, but may require optimization for high data volume needs.
- **Advantages**:
    - Wide coverage of cryptocurrency assets and exchanges.
    - Easy access without registration.
- **Disadvantages**:
    - Lower request rate limit compared to Binance API.
    - Limited functionality for real-time data due to rate limit constraints.

## Decision
For this project, a combination of **Binance API** and **CoinGecko API** will be used:

- **Binance API** is recommended for actively gathering real-time cryptocurrency price data and for solutions requiring a higher request rate.

## Rationale
- **Binance API** provides high speed and stability, suitable for real-time trading solutions due to its high request rate.
- **CoinGecko API** has a lower request limit and fewer available assets, making it less suitable for project needs.

## Chosen Option
Use **Binance API**.

## API Documentation Links
- [Binance API Documentation](https://binance-docs.github.io/apidocs/spot/en/)
- [CoinGecko API Documentation](https://www.coingecko.com/en/api)
