import 'scss/_partners.scss';

import avcrsLogo from 'assets/partners/avcrs.png';
import inQoolLogo from 'assets/partners/inqool.png';
import aisLogo from 'assets/partners/ais.png';

export const Partners = () => (
	<div className="partners">
		<a
			className="partner"
			target="_blank"
			rel="noopener noreferrer"
			href="https://www.inqool.cz"
			title="inQool"
		>
			<img src={inQoolLogo} alt="Logo inQool" />
		</a>
		<a
			className="partner"
			target="_blank"
			rel="noopener noreferrer"
			href="http://www.aiscr.cz/en/"
			title="Archaeological Information System
of the Czech Republic"
		>
			<img
				src={aisLogo}
				alt="Logo Archaeological Information System
of the Czech Republic"
			/>
		</a>
		<a
			className="partner"
			target="_blank"
			rel="noopener noreferrer"
			href="http://www.avcr.cz/cs/strategie/vyzkumne-programy/prehled-programu/index.html"
			title="Akademie věd České republiky strategie"
		>
			<img src={avcrsLogo} alt="Logo Akademie věd České republiky strategie" />
		</a>
	</div>
);
