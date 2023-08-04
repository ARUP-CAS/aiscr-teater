import { useMemo } from 'react';
import showdown from 'showdown';

const converter = new showdown.Converter({
	openLinksInNewWindow: true,
});

type Props = {
	text: string;
	inline?: boolean;
};

const Markdown = ({ text, inline }: Props) => {
	const md = useMemo(() => converter.makeHtml(text), [text]);
	return md ? (
		<span
			dangerouslySetInnerHTML={{ __html: md }}
			className={inline ? 'markdown-inline' : undefined}
		/>
	) : null;
};

export default Markdown;
