# Virhon Unified OpenBanking Gateway
This project aims to create a universal API for accessing banks via the OpenBanking interface (UOBG). The target audience of this project is TPPs holding AISP, PISP licenses, or both.

## Product
The product of this project is software that deploys as a Docker container within the TPP's perimeter and unifies access to bank APIs.

## Target Market
Initially, the product focuses on Ukrainian TPPs, but it has the potential to expand worldwide.

## Core Value
The key value of this product:

1. UOBG serves solely as an access channel to bank APIs and does not store users' financial information.

2. It deploys within the TPP's perimeter, meaning TPPs do not need to share their keys and certificates with an intermediary.

3. It allows TPPs to save money on developing integrations with each individual bank using their own resources.

4. The core of the product is metadata-driven, which simplifies adding new integrations.

## Architecture
![Principal architecture](./unified-openbanking-gateway-principal-01.drawio.png)
1. UOBG is not a SaaS; it deploys within the TPP's perimeter.

2. GatewayCore is the core of the system. It is responsible for generating requests to banks and parsing responses from banks according to metadata.

3. BanksConnector is a sidecar responsible for direct communication with banks, applying signatures, and establishing an mTLS connection.

4. QWAC, QSealC are TPP certificates stored within the TPP's perimeter.

5. Metadata is the storage that holds the rules for transforming requests and responses.