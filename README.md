# 🌐 SciSearch — Backend

This repository contains the backend API for **SciSearch**, developed with **Spring Boot**. It handles user authentication, integration with external scientific data sources, management of favorite articles, and all core business logic.

**Frontend repository:**
>https://github.com/lucianoomayer/scisearch-frontend

**Live version (Vercel):**
>https://scisearch-lucianoomayer.vercel.app

**Note:** Because the backend runs on a platform with cold start, the first request — or any request after a period of inactivity — may take a few seconds to respond.

## Architecture Overview

- Spring Boot
- Spring Security + JWT
- Spring WebFlux / WebClient
- PostgreSQL
- Deployed on Render

## Features

- **User Authentication**
  Secure user registration and login using JWT.

- **Article Retrieval**
  Retrieves article data from multiple external APIs.

- **Favorites Management**
  Save, remove, and retrieve user favorite articles.

- **Database Integration**
  Persistent data storage using PostgreSQL hosted on Neon.

## External Data Sources

- **PubMed** — https://pubmed.ncbi.nlm.nih.gov
- **CrossRef** — https://api.crossref.org

>More data sources will be added as SciSearch evolves.
