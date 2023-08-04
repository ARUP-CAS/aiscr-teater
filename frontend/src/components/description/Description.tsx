import { FC, forwardRef, Fragment } from 'react';
import times from 'lodash/times';
import { MdArchive, MdLink, MdOpenInNew } from 'react-icons/md';
import { useLocation } from 'react-router';

import Column from 'components/column';
import { StyledContent } from 'components/StyledContent';
import {
	GetDescriptionPage_singleCategoryWithUrl_descriptions,
	GetDescriptionPage_singleCategoryWithUrl_descriptions_contents,
	GetDescriptionPage_singleCategoryWithUrl_descriptions_contents_quotes,
} from 'generated/GetDescriptionPage';
import useTranslation from 'hooks/useTranslation';
import Markdown from 'components/Markdown';

import { DescriptionData } from './_types';

const descriptionHasContent = (
	d: GetDescriptionPage_singleCategoryWithUrl_descriptions,
) => d.contents?.length && d.contents.some(contentHasContent);

const contentHasContent = (
	c: GetDescriptionPage_singleCategoryWithUrl_descriptions_contents,
) => c.text || (c.quotes?.length && c.quotes.some(quoteHasContent));

const quoteHasContent = (
	q: GetDescriptionPage_singleCategoryWithUrl_descriptions_contents_quotes,
) => q.source;

const DescriptionLoading: FC = () => (
	<div className="description-content">
		<div className="loading-background-meta" />
		{times(6, index => (
			<div key={index} className="loading-background" />
		))}
	</div>
);

const DescriptionContent = forwardRef<
	HTMLHeadingElement,
	Required<Pick<DescriptionData, 'id' | 'title' | 'descriptions'>>
>(({ id, title, descriptions }, ref) => {
	const { search } = useLocation();

	const t = useTranslation({
		availableFrom: {
			cs: 'Dostupné z:',
			en: 'Available from:',
			de: 'Verfügbar ab:',
		},
		export: {
			cs: 'Exportovat',
			en: 'Export',
			de: 'Export',
		},
	});

	return (
		<div className="description-content">
			<a className="export" href={`/api/export/${id}`} download>
				{t.export}
				<MdArchive />
			</a>
			<h2 ref={ref}>{title}</h2>
			<a
				className="permalink"
				href={`${window.location.origin}/id/${id}${search}`}
			>
				<MdLink /> {window.location.origin}/id/{id}
				{search}
			</a>
			{descriptions ? (
				<StyledContent>
					{descriptions.filter(descriptionHasContent).map(d => (
						<Fragment key={d.id}>
							<strong>{d.title}</strong>
							<ul>
								{d.contents?.filter(contentHasContent).map(c => (
									<li key={c.id}>
										<Markdown text={c.text ?? ''} />
										{c.quotes?.length && c.quotes.some(quoteHasContent) && (
											<ul>
												{c.quotes.filter(quoteHasContent).map(q => (
													<li key={q.id}>
														{q.title && `${q.title} In `}
														<Markdown inline text={q.source?.label ?? ''} />
														{q.locationPage && ` ${q.locationPage}`}
														{q.date && ` ${q.date}`}
														{(q.locationUrl || q.source?.url) && (
															<>
																{` ${t.availableFrom} `}
																<a
																	href={q.locationUrl ?? q.source?.url ?? ''}
																	target="_blank"
																	rel="noreferrer"
																>
																	{q.locationUrl ?? q.source?.url ?? ''}
																	<MdOpenInNew />
																</a>
															</>
														)}
													</li>
												))}
											</ul>
										)}
									</li>
								))}
							</ul>
						</Fragment>
					))}
				</StyledContent>
			) : (
				<div className="empty" />
			)}
		</div>
	);
});

export const Description = forwardRef<HTMLHeadingElement, DescriptionData>(
	(
		{
			id = '',
			neighborsCol,
			childrenCol,
			title = '',
			descriptions = [],
			loading = false,
		},
		ref,
	) => {
		const t = useTranslation({
			neighborsTitle: {
				cs: 'Výběr hesel',
				en: 'Term Selection',
				de: 'Suchbegriffe',
			},
			childrenTitle: {
				cs: 'Podřazená hesla',
				en: 'Narrower terms',
				de: 'Engere Suchbegriffe',
			},
		});
		return (
			<div className="description">
				<Column title={t.neighborsTitle} data={neighborsCol} />
				{loading ? (
					<DescriptionLoading />
				) : (
					<DescriptionContent
						id={id}
						title={title}
						descriptions={descriptions}
						ref={ref}
					/>
				)}
				{!!childrenCol.length && (
					<Column title={t.childrenTitle} data={childrenCol} />
				)}
			</div>
		);
	},
);
