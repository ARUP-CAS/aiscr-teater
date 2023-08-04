import { FC } from 'react';
import classNames from 'classnames';

import { Footer } from './Footer';

export const Page: FC<{ className?: string }> = ({
	children,
	className = '',
}) => (
	<div className={classNames('page', className)}>
		{children}
		<Footer />
	</div>
);
