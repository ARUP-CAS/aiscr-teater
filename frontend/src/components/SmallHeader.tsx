import { FC } from 'react';

import { SearchContainer } from 'containers/SearchContainer';

import { Header } from './header';
import { HeaderProps } from './header/_types';

export const SmallHeader: FC<HeaderProps> = props => (
		<Header {...props}>
			<div className="header-search">
				<SearchContainer />
			</div>
		</Header>
	);
