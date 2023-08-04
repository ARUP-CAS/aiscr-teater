import { FC } from 'react';
import classNames from 'classnames';

import { Logo } from '../Logo';
import { HeaderLinks } from '../HeaderLinks';
import { Slider } from '../Slider';

import { HeaderProps } from './_types';
import { HeaderTags } from './HeaderTags';

export const Header: FC<HeaderProps> = ({
	children,
	className,
	title = '',
	sliderImages = [],
	hideDefaultBackgroundImage = false,
}) => {
	const style = {
		...(hideDefaultBackgroundImage && {
			backgroundImage: 'none',
			backgroundColor: 'transparent',
		}),
	};

	return (
		<header
			className={classNames(className, { header: !className })}
			style={style}
		>
			<HeaderTags title={title} />
			<Slider source={sliderImages} />
			<Logo />
			{children}
			<HeaderLinks />
		</header>
	);
};
