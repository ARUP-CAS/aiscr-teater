import { FC } from 'react';
import { RouteComponentProps } from 'react-router';

import 'scss/pages/_page_hint.scss';
import 'scss/pages/_page_home.scss';

import { HintContent } from 'components/HintContent';
import { Page } from 'components/Page';
import { Content } from 'components/Content';
import { LargeHeaderContainer } from 'containers/LargeHeaderContainer';
import useTranslation from 'hooks/useTranslation';

const HintPage: FC<RouteComponentProps> = () => {
	const t = useTranslation({
		hint: { cs: 'Nápověda', en: 'Hint', de: 'Hinweise' },
	});

	return (
		<Page className="page-hint">
			<LargeHeaderContainer title="Domov" />
			<Content className="content-home">
				<h2>{t.hint}</h2>
				<HintContent />
			</Content>
		</Page>
	);
};

export default HintPage;
