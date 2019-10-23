import React from 'react'
import { ApolloProvider } from 'react-apollo-hooks'
import ApolloClient from 'apollo-boost'
import { AppRouter } from 'router/AppRouter'

const client = new ApolloClient({
  uri: '/api/graphql'
})

export function App(): JSX.Element {
  return (
    <ApolloProvider client={client}>
      <AppRouter />
    </ApolloProvider>
  )
}
