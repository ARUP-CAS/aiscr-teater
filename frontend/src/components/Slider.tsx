import { FC } from 'react';
import '../scss/_slider.scss';

const NUMBER_OF_SLIDES = 7;

export const Slider: FC<{ source: string[]; timeout?: number }> = ({
	source = [],
	timeout = 6,
}) => {
	if (source.length && source.length !== NUMBER_OF_SLIDES) {
		// eslint-disable-next-line no-console
		console.error(
			`Number of slides must be ${NUMBER_OF_SLIDES}. 
      Slider is defined by css and when number of slides are changed keyframes must be recalculated.
      File: scss/_slider.scss`,
		);
		return null;
	}

	return source.length ? (
		<ul className="slider">
			{source.map((src, index) => (
				<li
					className="slide"
					key={src}
					style={{
						backgroundImage: `url(${src})`,
						animationDelay: `${index * timeout}s`,
						animationDuration: `${source.length * timeout}s`,
					}}
				/>
			))}
		</ul>
	) : null;
};
