import { FC } from 'react';
import gql from 'graphql-tag';
import { useQuery } from 'react-apollo-hooks';
import { RouteComponentProps } from 'react-router';

import 'scss/pages/_page_home.scss';

import { Page } from 'components/Page';
import { LargeHeader } from 'components/LargeHeader';
import { Content } from 'components/Content';
import { GetHomePage, GetHomePageVariables } from 'generated/GetHomePage';
import Categories from 'components/categories';
import { Partners } from 'components/Partners';
import useTranslation from 'hooks/useTranslation';
import useLanguage from 'hooks/useLanguage';

import { OoopsError } from '../components/Ooops';
import { LargeHeaderContainer } from '../containers/LargeHeaderContainer';

const GET_HOME_PAGE = gql`
	query GetHomePage($language: Language!) {
		listRootCategories(language: $language) {
			id
			name
			url
		}
	}
`;

const HomePage: FC<RouteComponentProps> = () => {
	const language = useLanguage();
	const { data, loading, error } = useQuery<GetHomePage, GetHomePageVariables>(
		GET_HOME_PAGE,
		{
			variables: { language },
			fetchPolicy: 'no-cache',
		},
	);

	const t = useTranslation({
		home: { cs: 'Domov', en: 'Home', de: 'Startseite' },
		sectionsTitle: {
			cs: 'Prozkoumejte další oblasti',
			en: 'Explore other areas',
			de: 'Erkunden Sie andere Bereiche',
		},
		aboutTitle: {
			cs: 'Co je to TEATER',
			en: 'What is a TEATER',
			de: 'Über TEATER',
		},
		description: {
			cs: 'Tezaurus archeologické terminologie je webový tezaurus, který má za úkol zpřístupnit a přiblížit archeologickou terminologii a terminologii příbuzných oborů. TEATER je určen širokému okruhu uživatelů od laické veřejnosti, přes amatérské archeology, začínající studenty archeologie, knihovníky nejen archeologických institucí až po profesionální archeology. Hesla jsou dostupná v interaktivním rozhraní a je v nich možné buďto full-textově vyhledávat, nebo procházet hierarchicky. Obsah TEATERu je dostupný ve třech jazykových lokalizacích – češtině, angličtině a němčině. Hesla je možné z TEATERu exportovat ve formátu .json nebo je možné je linkovat za pomocí jejich PURL. Další informace ohledně možností využití tezauru různými skupinami uživatelů, o vyhledávací aplikaci, struktuře a výběru hesel se nacházejí v poli Nápověda.',
			en: 'The Thesaurus of Archaeological Terminology is a web thesaurus aimed at making the terminology of archaeology and the related disciplines more available. TEATER is intended for a wide sphere of users ranging from the lay public to amateur archaeologists, beginning students of archaeology, librarians of not only archaeological institutions and professional archaeologists. The entries are available in an interactive interface that enables both full-text searching and hierarchical browsing. TEATER’s content is available in three language versions – Czech, English and German. Entries can be exported from TEATER in .json format or linked using their PURL. More information on the possibilities of use of the thesaurus by various groups of users, the search application, the structure and selection of the entries is available in the Help section.',
			de: 'TEATER, der Thesaurus der archäologischen Terminologie, ist ein Online-Thesaurus, der die Terminologie der Archäologie und der verwandten Fächer nicht nur zugänglich machen, sondern auch näherbringen soll. Er richtet sich an ein breites Publikum, von der Laienöffentlichkeit über Hobby-Archäologen und angehende Archäologie-Studenten bis hin zu Bibliothekaren auch anderer Fachbereiche sowie an professionelle Archäologen. Die Suchbegriffe sind über eine interaktive Schnittstelle erreichbar, in der sowohl eine Volltextsuche wie auch ein hierarchischer Aufbau zur Verfügung stehen. Der Inhalt dieses Thesaurus wird in den Sprachen Deutsch, Tschechisch und Englisch angeboten. Die Schlagwörter können im json-Format exportiert oder mithilfe ihrer PURL verlinkt werden. Weitere Informationen hinsichtlich der Nutzungsmöglichkeiten des Thesaurus durch die verschiedenen Nutzergruppen sowie zu der Suchfunktion, der Struktur und der Auswahl der Suchbegriffe lassen sich über die Hilfe-Schaltfläche finden.',
		},
	});

	if (error) {
		return (
			<Page>
				<LargeHeader title={t.home} />
				<Content className="content-home">
					<OoopsError />
				</Content>
			</Page>
		);
	}

	return (
		<Page>
			<LargeHeaderContainer title={t.home} />
			<Content className="content-home">
				<h2>{t.sectionsTitle}</h2>
				<Categories
					data={data ? data.listRootCategories : []}
					loading={loading}
				/>
				<h2>{t.aboutTitle}</h2>
				<p className="description">{t.description}</p>
				<Partners />

				<div
					className="cc"
					dangerouslySetInnerHTML={{
						__html: `
							<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">
								<img alt="Licence Creative Commons" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" />
							</a>
							<br />
							<a
								xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Dataset" property="dct:title" rel="dct:type">TEATER - data files
							</a>, jehož autorem je 
							<span
								xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Archeologický ústav AV ČR, Brno, v. v. i.
							</span>, podléhá licenci 
							<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">Creative Commons Uveďte původ-Neužívejte komerčně 4.0 Mezinárodní </a>.
						`,
					}}
				/>
			</Content>
		</Page>
	);
};
export default HomePage;
