import { FC } from 'react';
import { RouteComponentProps } from 'react-router-dom';

import 'scss/pages/_page_description.scss';
import { Page } from 'components/Page';
import { Content } from 'components/Content';
import DescriptionContainer from 'containers/DescriptionContainer';
import useLanguage from 'hooks/useLanguage';

import { SmallHeader } from '../components/SmallHeader';

const DescriptionPage: FC<RouteComponentProps> = ({
	location: { pathname },
}) => {
	const language = useLanguage();
	return (
		<Page>
			<SmallHeader />
			<Content fluid className="content-description-page">
				<DescriptionContainer language={language} url={pathname.slice(1)} />
			</Content>
		</Page>
	);
};
export default DescriptionPage;
