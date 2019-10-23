import React, { lazy, Suspense } from 'react'
import { Router, Route, Switch, RouteComponentProps } from 'react-router-dom'
import { history } from './history'
import { DimmerLoading } from 'components/DimmerLoading'

function lazyPage(fileName: string): React.ComponentType<RouteComponentProps> {
  const Component = lazy(() => import(`pages/${fileName}`))

  // eslint-disable-next-line react/display-name
  return (props: RouteComponentProps) => (
    <Suspense fallback={<DimmerLoading />}>
      <Component {...props} />
    </Suspense>
  )
}

export function AppRouter(): JSX.Element {
  return (
    <Router history={history}>
      <Switch>
        <Route path="/" exact component={lazyPage('HomePage')} />
        <Route path="/hint" exact component={lazyPage('HintPage')} />
        <Route
          path="/search/:searchValue?"
          component={lazyPage('SearchPage')}
        />
        <Route component={lazyPage('DescriptionPage')} />
      </Switch>
    </Router>
  )
}
