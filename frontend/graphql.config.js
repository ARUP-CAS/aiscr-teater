module.exports = {
  schema: '../backend/src/main/resources/graphql/**/*.graphqls',
  documents: './src/**/*.{ts,tsx}',
  extensions: {
    endpoints: {
      'Default GraphQL Endpoint': {
        url: 'http://localhost:8080/api/graphql',
        headers: {
          'user-agent': 'JS GraphQL'
        },
        introspect: false
      }
    }
  }
}