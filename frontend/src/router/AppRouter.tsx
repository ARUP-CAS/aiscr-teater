import { lazy, Suspense } from 'react';
import {
	BrowserRouter as Router,
	Route,
	Switch,
	RouteComponentProps,
} from 'react-router-dom';

import { DimmerLoading } from 'components/DimmerLoading';

const lazyPage = (fileName: string) => {
	const Component = lazy(() => import(`pages/${fileName}`));
	return (props: RouteComponentProps) => (
		<Suspense fallback={<DimmerLoading />}>
			<Component {...props} />
		</Suspense>
	);
};

export const AppRouter = () => (
	<Router>
		<Switch>
			<Route path="/" exact component={lazyPage('HomePage')} />
			<Route path="/hint" exact component={lazyPage('HintPage')} />
			<Route
				path="/search/:searchValue?"
				component={lazyPage('SearchPage')}
			/>
			<Route path="/id/:id" component={lazyPage('PermalinkPage')} />
			<Route component={lazyPage('DescriptionPage')} />
		</Switch>
	</Router>
);
