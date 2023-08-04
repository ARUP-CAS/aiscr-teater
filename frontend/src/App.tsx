import { ApolloProvider } from 'react-apollo-hooks';
import ApolloClient from 'apollo-boost';

import { AppRouter } from 'router/AppRouter';

const client = new ApolloClient({
  uri: '/api/graphql'
});

export const App = () => (
    <ApolloProvider client={client}>
      <AppRouter />
    </ApolloProvider>
  );
